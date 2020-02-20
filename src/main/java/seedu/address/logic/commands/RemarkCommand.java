package seedu.address.logic.commands;

import seedu.address.model.Model;

import static java.util.Objects.requireNonNull;

/**
 * RemarkCommand represents a command given by
 * the user to modify a remark in the address book
 */
public class RemarkCommand extends Command {
    public static final String COMMAND_WORD = "remark";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return new CommandResult("Added the remark (not)");
    }
}
