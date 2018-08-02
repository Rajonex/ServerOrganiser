package service;

import java.util.List;

import dao.DAOFactory;
import dao.GroupDAO;
import dao.LessonDAO;
import dao.StudentDAO;
import sends.Ack;
import sends.Group;
import sends.Lesson;
import sends.MiniLesson;
import utils.LessonError;

public class LessonService {

	public Ack addLesson(Lesson lesson)
	{

		DAOFactory factory = DAOFactory.getDAOFactory();
		LessonDAO lessonDao = factory.getLessonDAO();
		GroupDAO groupDao = factory.getGroupDAO();
		Group group = groupDao.read(lesson.getGroupId());
		int result = -1;
		if (group != null)
		{
			result = lessonDao.safeCreate(lesson, group.getRate());
		}
		Ack ack = new Ack();

		switch (result)
		{
		case 0:
			ack.setConfirm(true);
			ack.setMessage("Msg");
			break;
		case 1:
			ack.setConfirm(false);
			ack.setMessage(LessonError.LESSON.toString());
			break;
		case 2:
			ack.setConfirm(false);
			ack.setMessage(LessonError.PRESENCE.toString());
			break;
		case -1:
			ack.setConfirm(false);
			ack.setMessage(LessonError.GROUP_ID.toString());
			break;
		case 3:
			ack.setConfirm(false);
			ack.setMessage(LessonError.PAYMENT.toString());
			break;
		default:
			ack.setConfirm(false);
			ack.setMessage(LessonError.OTHER.toString());
		}
		return ack;
	}
	
	public Lesson getLessonById(long lessonId, String token)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		LessonDAO lessonDao = factory.getLessonDAO();
		StudentDAO studentDao = factory.getStudentDAO();
		Lesson lesson = lessonDao.getLessonByTokenAndId(lessonId, token);
		if(lesson != null)
		{
			lesson.setStudentPresent(studentDao.getPresenceForLesson(lesson.getId()));
		}
		return lesson;
	}
	
	public List<MiniLesson> getMiniLessons(long groupId, String token)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		LessonDAO lessonDao = factory.getLessonDAO();
		return lessonDao.getGroupsMiniLessons(groupId, token);
	}
	
	public boolean updateLesson(Lesson lesson)
	{
		DAOFactory factory = DAOFactory.getDAOFactory();
		LessonDAO lessonDao = factory.getLessonDAO();
		GroupDAO groupDao = factory.getGroupDAO();
		Group group = groupDao.read(lesson.getGroupId());
		if (group != null)
		{
			return lessonDao.updateAll(lesson, group.getRate());
		}
		return false;
	}
}
