package main;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import impl.BooksTable;
import impl.DatabaseService;

@SpringBootApplication
@EnableJpaRepositories("impl")
@ComponentScan(basePackages = "impl")
@EntityScan(basePackages = "impl")
public class Application {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Application.class, args);
	}
	
	@Component
    class RestApi extends RouteBuilder {
        @Override
        public void configure() {
            restConfiguration()
                .contextPath("/camel-rest-jpa").apiContextPath("/api-doc")
                    .apiProperty("api.title", "Camel REST API")
                    .apiProperty("api.version", "1.0")
                    .apiProperty("cors", "true")
                    .apiContextRouteId("doc-api")
                .bindingMode(RestBindingMode.json);

            rest("/books").description("Books REST service")
                .get("/getAll").description("The list of all the books")
                    .route().routeId("books-api")
                    .bean(DatabaseService.class, "findBooks")
                    .endRest()
	            .get("/getBook/{id}").description("Find book by ID")
		            .outType(BooksTable.class)
		            .param().name("id").type(path).description("The ID of the book").dataType("String").endParam()
		            .responseMessage().code(200).message("Book successfully returned").endResponseMessage()
		            .to("bean:databaseService?method=getBook(${header.id})")
	            .put("/updateBook/{id}").description("Update a book").type(BooksTable.class)
		            .param().name("id").type(path).description("The ID of the book to update").dataType("String").endParam()    
		            .param().name("body").type(body).description("The book to update").endParam()
		            .responseMessage().code(200).message("Book successfully updated").endResponseMessage()
		            .to("bean:databaseService?method=updateBook")
            	.delete("/deleteBook/{id}").description("Delete a book")
            		.param().name("id").type(path).description("The ID of the book to delete").dataType("String").endParam() 
            		.responseMessage().code(200).message("Book successfully deleted").endResponseMessage()
            		.to("bean:databaseService?method=deleteBook(${header.id})")
            	.post("/addBook").description("Add a book").type(BooksTable.class)
            		.param().name("body").type(body).description("The book to add").endParam()
            		.responseMessage().code(200).message("Book successfully added").endResponseMessage()
            		.to("bean:databaseService?method=addBook");
        }
    }
}
