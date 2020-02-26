package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

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
            + "Parameters: INDEX (must be a positive integer) "
            + "r/ [REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "r/ Friendly.";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Remark: %2$s";

    private final Index index;
    private final String remark;

    /**
     * Constructor for a new RemarkCommand. Takes in the
     * Index of the person to modify, as well as a String
     * containing the actual remark.
     *
     * @param ind Index of a person in the filtered person list
     * @param remark The remark to be updated
     */
    public RemarkCommand(Index ind, String remark) {
        requireAllNonNull(ind, remark);
        this.index = ind;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        throw new CommandException(String.format(MESSAGE_ARGUMENTS, index.getOneBased(), remark));
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
