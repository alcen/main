package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

/**
 * RemarkCommand represents a command given by
 * the user to modify a remark in the address book
 */
public class RemarkCommand extends Command {
    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a remark to a person, or overwrites the old "
            + "remark if the person already has one.\nThe person is "
            + "referred to by the index number used in the "
            + "displayed person list.\n"
            + "If no remark is specified, the remark is removed.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[r/ REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "r/ Friendly.";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added remark to Person: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed remark from Person: %1$s";

    private final Index index;
    private final Remark remark;

    /**
     * Constructor for a new RemarkCommand. Takes in the
     * Index of the person to modify, as well as a String
     * containing the actual remark.
     *
     * @param ind Index of a person in the filtered person list
     * @param remark The remark to be updated
     */
    public RemarkCommand(Index ind, Remark remark) {
        requireAllNonNull(ind, remark);
        this.index = ind;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX));
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), remark);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message based on whether the remark is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !remark.isBlank()
                ? MESSAGE_ADD_REMARK_SUCCESS
                : MESSAGE_DELETE_REMARK_SUCCESS;
        return String.format(message, personToEdit);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof RemarkCommand) {
            RemarkCommand rc = (RemarkCommand) o;
            return this.index.equals(rc.index)
                    && this.remark.equals(rc.remark);
        } else {
            return false;
        }
    }
}
