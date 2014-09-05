package com.iorga.bblorganizer.ws;

import org.apache.commons.io.IOUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@ApplicationScoped
@Path("/baggers")
public class BaggersWS {

	@GET
	@Path("")
	public String getBaggers() throws MalformedURLException, IOException {
		String baggers = IOUtils.toString(new URL("http://www.brownbaglunch.fr/js/baggers.js").openStream());

		int firstIndex = baggers.indexOf('{');
		int lastIndex = baggers.lastIndexOf('}');
		return baggers.substring(firstIndex, lastIndex+1);
	}
}
