package models;

import java.util.ArrayList;
import java.util.List;

public class QuestionDTO {

	public static String name;
	public static String mail;
	public static String content;

    public static List<String> all() {
    	List<String> question = new ArrayList<>();

    	question.add(name);
    	question.add(mail);
    	question.add(content);

        return question;
    }

	public static void update(QuestionDTO question) {
    	name= question.name;
    	content = question.content;
    	mail = question.mail;
    }

    public static String getName() {
		return name;
	}

	public static void setName(String name) {
		QuestionDTO.name = name;
	}

	public static String getMail() {
		return mail;
	}

	public static void setMail(String mail) {
		QuestionDTO.mail = mail;
	}

	public static String getContent() {
		return content;
	}

	public static void setContent(String content) {
		QuestionDTO.content = content;
	}
}
