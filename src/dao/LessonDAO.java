package dao;

import java.util.List;

import sends.Lesson;
import sends.MiniLesson;

public interface LessonDAO extends GenericDAO<Lesson, Long> {

	public boolean updateAll(Lesson updateObject, double rate);
	public int safeCreate(Lesson lesson, double lessonCost);
	public List<Lesson> getAll();
	public List<MiniLesson> getGroupsMiniLessons(long groupId, String token);
	public Lesson getLessonByTokenAndId(long id, String token);
}
