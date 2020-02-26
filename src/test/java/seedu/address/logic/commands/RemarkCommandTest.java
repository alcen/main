package seedu.address.logic.commands;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

public class RemarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemarkUnfilteredList_success() {
        Person originalPerson = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        Person remarkedPerson = new PersonBuilder(originalPerson)
                .withRemark(VALID_REMARK_AMY).build();

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, remarkedPerson.getRemark());
        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, remarkedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()), remarkedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }
}