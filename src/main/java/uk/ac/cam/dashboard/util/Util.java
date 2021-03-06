package uk.ac.cam.dashboard.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.cam.dashboard.models.Mappable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class Util {
	
	public static List<Map<String, ?>> getImmutableCollection(Set<? extends Mappable> raws) {
		List<Map<String,?>> immutalizedCollection= new ArrayList<Map<String, ?>>(0);
		for(Mappable raw: raws)
			immutalizedCollection.add(raw.toMap());
		return immutalizedCollection;
	}
	
	public static <T extends Mappable> Set<Integer> getIds(List<T> collection) {
		Set<Integer> ids = new HashSet<Integer>();
		for(T item : collection) {
			ids.add(item.getId());
		}
		
		return ids;
	}
	
	public static <T extends Mappable> T findById(List<T> collection, int id) {
		for(T element: collection) {
			if (element.getId() == id) {
				return element;
			}
		}
		
		return null;
	}
	
	public static <T, K> ImmutableMap<T, List<K>> multimapToImmutableMap(ArrayListMultimap<T, K> mm) {
		ImmutableMap.Builder<T, List<K>> builder = ImmutableMap.builder();
				
		for(T k : mm.keySet()) {
			builder.put(k, ImmutableList.copyOf(mm.get(k)));
		}
			
		return builder.build();
	}
}
