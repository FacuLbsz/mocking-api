package faka.cs.route;

public class HttpDocumentRouteException extends Exception {

	private static final long serialVersionUID = -4735310145113668204L;
	
	public HttpDocumentRouteException(String message) {
		super(message);
	}
	
	public HttpDocumentRouteException(Throwable t) {
		super(t);
	}

}
