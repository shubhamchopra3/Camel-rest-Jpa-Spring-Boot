package impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service("databaseService")
public class DatabaseService {
	@Autowired
    BookRepository books;

    public Iterable<BooksTable> findBooks() {
        return books.findAll();
    }
    public BooksTable getBook(String id) {
		return books.findOne(id);
	}
	
	public void addBook(BooksTable b1) {
		books.save(b1);
	}
	
	public void updateBook(BooksTable b1, String id) {
		books.save(b1);
	}
	
	public void deleteBook(String id) {
		books.delete(id);
	}
}
