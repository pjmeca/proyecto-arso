package arso.especificacion;

public interface Specificable<T> {

    boolean satisfies(Specification<T> specification);
}
