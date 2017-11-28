package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import com.avaje.ebean.annotation.CreatedTimestamp;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Pattern;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class User extends Model{
	@Id
	public Long userId;
	@Required
	@MaxLength(10)
	public String userName;
	@Required
	@MaxLength(10)
	@Pattern(value="^[0-9a-zA-Z]*$")
	public String password;
	@Required
	@Email
	public String mail;
	@CreatedTimestamp
	public Date createDate;

	@Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    public List<Distance> distance = new ArrayList<Distance>();

	public static Finder<Long, User> finder = new Finder<Long, User>(Long.class, User.class);

    public static List<User> all() {
        return finder.all();
    }

    public static User byId(String id) {
    	return finder.byId(Long.parseLong(id));
    }

    public static void create(User user) {
    	user.save();
    }

    public static void update(User user , Long id) {
    	User newuser = finder.byId(id);
    	newuser.userName= user.userName;
    	newuser.password = user.password;
    	newuser.mail = user.mail;
        newuser.update();
    }
}
