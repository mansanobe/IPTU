package main.db;

public interface IPersistent {

	int getID();
	void setID(int id);
	void save();
	void delete();
}
