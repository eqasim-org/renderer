package ch.ethzm.matsim.renderer.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.matsim.api.core.v01.network.Link;

public class LinkDatabase {
	final private List<Link> links = new ArrayList<>();

	public LinkDatabase() {
	}

	public int addLink(Link link) {
		links.add(link);
		return links.size() - 1;
	}

	public Link getLink(int index) {
		return links.get(index);
	}

	public int getSize() {
		return links.size();
	}

	public int getIndex(Link link) {
		return links.indexOf(link);
	}

	public Collection<Link> getLinks() {
		return links;
	}
}
