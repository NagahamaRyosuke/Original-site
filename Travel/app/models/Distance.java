package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class Distance extends Model {

	@Id
	public Long distanceId;

	public String distanceTitle;

	@ManyToOne
    @JoinColumn(name = "userId")
    public User user;

    public static Finder<Long, Distance> finder = new Finder<Long, Distance>(Long.class, Distance.class);

    public static String title(String userId) {
    	Long id = Long.parseLong(userId);

    		List<Distance> dis = Distance.finder.where().eq("userId", id).findList();
        	Long max = (long) 0;
        	for(Distance num: dis) {
        		if(max <= num.distanceId) {
        			max = num.distanceId;
        		}
        	}
        	return finder.where().eq("distanceId", max).findList().get(0).distanceTitle;


    }
}
