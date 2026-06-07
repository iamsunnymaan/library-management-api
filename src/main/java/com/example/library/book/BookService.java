package com.example.library.book;

import com.example.library.book.dto.BookRequest;
import com.example.library.book.dto.BookResponse;
import com.example.library.common.ConflictException;
import com.example.library.common.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findBooks(String keyword, String category, Boolean available) {
        Specification<Book> spec = Specification.where(null);

        if (keyword != null && !keyword.isBlank()) {
            String like = "%" + keyword.trim().toLowerCase() + "%";
            spec = spec.and((root, query, builder) -> builder.or(
                    builder.like(builder.lower(root.get("title")), like),
                    builder.like(builder.lower(root.get("author")), like),
                    builder.like(builder.lower(root.get("isbn")), like)
            ));
        }

        if (category != null && !category.isBlank()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(builder.lower(root.get("category")), category.trim().toLowerCase()));
        }

        if (available != null) {
            spec = spec.and((root, query, builder) -> available
                    ? builder.greaterThan(root.get("availableCopies"), 0)
                    : builder.equal(root.get("availableCopies"), 0));
        }

        return bookRepository.findAll(spec, Sort.by("title").ascending())
                .stream()
                .map(BookResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookResponse getBook(Long id) {
        return BookResponse.from(getBookEntity(id));
    }

    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new ConflictException("A book with this ISBN already exists.");
        }
        Book book = new Book(
                request.title().trim(),
                request.author().trim(),
                request.isbn().trim(),
                normalizeOptional(request.category()),
                request.totalCopies()
        );
        return BookResponse.from(bookRepository.save(book));
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = getBookEntity(id);
        if (bookRepository.existsByIsbnAndIdNot(request.isbn(), id)) {
            throw new ConflictException("A book with this ISBN already exists.");
        }
        book.setTitle(request.title().trim());
        book.setAuthor(request.author().trim());
        book.setIsbn(request.isbn().trim());
        book.setCategory(normalizeOptional(request.category()));
        book.changeTotalCopies(request.totalCopies());
        return BookResponse.from(book);
    }

    public void deleteBook(Long id) {
        Book book = getBookEntity(id);
        if (book.getAvailableCopies() != book.getTotalCopies()) {
            throw new ConflictException("Cannot delete a book while copies are borrowed.");
        }
        bookRepository.delete(book);
    }

    public Book getBookEntity(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
    }

    private String normalizeOptional(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}

