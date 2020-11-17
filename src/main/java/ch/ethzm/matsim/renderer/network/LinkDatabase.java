package ch.ethzm.matsim.renderer.network;

import java.util.ArrayList;
import java.util.Collection;

import org.matsim.api.core.v01.network.Link;

public class LinkDatabase {
	final private ArrayList<Link> links = new ArrayList<>(100000);

	public LinkDatabase() {
	}

	public int addLink(Link link) {
		links.ensureCapacity(link.getId().index() + 10000);

		while (links.size() <= link.getId().index()) {
			links.add(null);
		}

		links.set(link.getId().index(), link);
		return link.getId().index();
	}

	public Link getLink(int index) {
		return links.get(index);
	}

	public int getSize() {
		return links.size();
	}

	public int getIndex(Link link) {
		return link.getId().index();
	}

	public Collection<Link> getLinks() {
		return links;
	}
}
