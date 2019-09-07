package faka.cs.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import faka.cs.model.http.HttpDocument;

public interface HttpDocumentRepository extends MongoRepository<HttpDocument, String> {
	
	public static final String POST = "POST";
	public static final String GET = "GET";
	
	public List<HttpDocument> findByMethod(String method);
	
	public HttpDocument findByUrlAndMethod(String url, String method);

}
