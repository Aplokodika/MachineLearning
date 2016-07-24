package neuralNetwork;

public interface Factory<T> {
	T newElement();
	T newElement(T value);
	
}
