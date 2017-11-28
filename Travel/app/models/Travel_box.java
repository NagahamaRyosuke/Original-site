package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Travel_box extends Model {

	@Id
	public Long id;

	public String date;
	public String start_time;
	public String end_time;
	public String title;
	public String place;
	public Long distanceId;

	public static Finder<Long, Travel_box> finder = new Finder<Long, Travel_box>(Long.class, Travel_box.class);

    public static List<Travel_box> all(String userId) {
    	Long id = Long.parseLong(userId);
    	List<Distance> dis = Distance.finder.where().eq("userId", id).findList();
    	Long max = (long) 0;
    	for(Distance num: dis) {
    		if(max <= num.distanceId) {
    			max = num.distanceId;
    		}
    	}
        return finder.where().eq("distanceId", max).findList();
    }

    public static void create(Travel_box box, Long id) {
    	if(Distance.finder.where().eq("userId", id).findList().size() != 0) {
        	List<Distance> dis = Distance.finder.where().eq("userId", id).findList();
        	Long max = (long) 0;
        	for(Distance num: dis) {
        		if(max <= num.distanceId) {
        			max = num.distanceId;
        		}
        	}
        	box.distanceId = max;
            box.save();
        }
    }

    public static void update(Travel_box box , Long id) {
    	Travel_box newbox = finder.byId(id);
    	newbox.date = box.date;
    	newbox.title = box.title;
    	newbox.start_time = box.start_time;
    	newbox.end_time = box.end_time;
    	newbox.place = box.place;
        newbox.update();
    }

    public static void delete(Long id) {
        finder.ref(id).delete();
    }

}
