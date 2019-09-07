package faka.cs.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

import faka.cs.model.http.HttpDocument;
import faka.cs.service.HttpValidator;
import faka.cs.utils.CamelUtils;

public class DynamicHttpDocumentRouteBuilder extends RouteBuilder {

	private HttpDocument httpDocument;
	private HttpValidator httpValidator;

	public DynamicHttpDocumentRouteBuilder(CamelContext context, HttpValidator httpValidator,
			HttpDocument httpDocument) {
		super(context);
		this.httpDocument = httpDocument;
		this.httpValidator = httpValidator;
	}

	@Override
	public void configure() throws Exception {
		CamelUtils.buildRoute(this, httpDocument, httpValidator);
	}
}