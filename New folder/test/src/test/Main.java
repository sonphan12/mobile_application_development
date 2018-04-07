package test;

public class Main {
	public static void main(String[] args) {
		IAnimal cat = new Cat();
		IAnimal dog = new Dog();
		Zoo zoo = new Zoo();
		zoo.addAnimal(cat);
		zoo.addAnimal(dog);
		zoo.sayAllHello();
	}
}
