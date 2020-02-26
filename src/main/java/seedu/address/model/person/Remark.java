package seedu.address.model.person;

/**
 * Represents a Person's remark in the address book
 * Remarks are always immutable, and can be constructed
 * with any possible Java String
 */
public class Remark {
    public final String value;

    public Remark(String rem) {
        this.value = rem;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o == this
                || (o instanceof Remark
                && value.equals(((Remark) o).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
