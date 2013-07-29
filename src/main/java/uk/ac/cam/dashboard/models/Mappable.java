package uk.ac.cam.dashboard.models;

import java.util.Map;

public interface Mappable {
	public int getId();
	public Map<String, ?> toMap();
}
