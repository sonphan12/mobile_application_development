package test;

import java.util.ArrayList;

public class Zoo {
	public ArrayList<IAnimal> listAnimal;
	public Zoo() {
		listAnimal = new ArrayList<>();
	}
	public void addAnimal(IAnimal animal) {
		listAnimal.add(animal);
	}
	public void sayAllHello() {
		for (IAnimal animal : listAnimal) {
			animal.sayHello();
		}
	}
}
