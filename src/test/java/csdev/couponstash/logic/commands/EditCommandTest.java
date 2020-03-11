package csdev.couponstash.logic.commands;

import static csdev.couponstash.logic.commands.CommandTestUtil.assertCommandFailure;
import static csdev.couponstash.logic.commands.CommandTestUtil.assertCommandSuccess;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import csdev.couponstash.commons.core.Messages;
import csdev.couponstash.commons.core.index.Index;
import csdev.couponstash.logic.commands.EditCommand.EditPersonDescriptor;
import csdev.couponstash.model.AddressBook;
import csdev.couponstash.model.Model;
import csdev.couponstash.model.ModelManager;
import csdev.couponstash.model.UserPrefs;
import csdev.couponstash.model.coupon.Coupon;
import csdev.couponstash.testutil.EditPersonDescriptorBuilder;
import csdev.couponstash.testutil.PersonBuilder;
import csdev.couponstash.testutil.TypicalIndexes;
import csdev.couponstash.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Coupon editedCoupon = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedCoupon).build();
        EditCommand editCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedCoupon);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedCoupon);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Coupon lastCoupon = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastCoupon);
        Coupon editedCoupon = personInList.withName(CommandTestUtil.VALID_NAME_BOB)
                .withPhone(CommandTestUtil.VALID_PHONE_BOB)
                .withTags(CommandTestUtil.VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(CommandTestUtil.VALID_NAME_BOB)
                .withPhone(CommandTestUtil.VALID_PHONE_BOB).withTags(CommandTestUtil.VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedCoupon);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastCoupon, editedCoupon);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Coupon editedCoupon = model.getFilteredPersonList().get(TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedCoupon);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        CommandTestUtil.showPersonAtIndex(model, TypicalIndexes.INDEX_FIRST_PERSON);

        Coupon couponInFilteredList = model.getFilteredPersonList()
                .get(TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased());
        Coupon editedCoupon = new PersonBuilder(couponInFilteredList).withName(CommandTestUtil.VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(CommandTestUtil.VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedCoupon);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedCoupon);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Coupon firstCoupon = model.getFilteredPersonList().get(TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstCoupon).build();
        EditCommand editCommand = new EditCommand(TypicalIndexes.INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        CommandTestUtil.showPersonAtIndex(model, TypicalIndexes.INDEX_FIRST_PERSON);

        // edit coupon in filtered list into a duplicate in address book
        Coupon couponInList = model.getAddressBook().getPersonList()
                .get(TypicalIndexes.INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(couponInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(CommandTestUtil.VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        CommandTestUtil.showPersonAtIndex(model, TypicalIndexes.INDEX_FIRST_PERSON);
        Index outOfBoundIndex = TypicalIndexes.INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(CommandTestUtil.VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand =
                new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(CommandTestUtil.DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(
                new EditCommand(TypicalIndexes.INDEX_SECOND_PERSON, CommandTestUtil.DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(
                new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.DESC_BOB)));
    }

}