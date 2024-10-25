package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonWithRoleDescriptorBuilder;

public class RoleCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addValidTag_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());

        Person personToTag = model.getFilteredPersonList().get(0); // Assuming Alice is the first person

        // Define the new tag to add
        String newTag = "friend";

        // Create the tag command descriptor with one tag
        RoleCommand.PersonWithRoleDescriptor descriptor = new PersonWithRoleDescriptorBuilder().withRole(newTag).build();

        RoleCommand tagCommand = new RoleCommand(indexLastPerson, null, descriptor);

        // Create the expected person with the new tags as strings
        Person expectedPerson = new PersonBuilder(personToTag).withRole(newTag).build();

        // Setup the expected model
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToTag, expectedPerson);

        // Execute the command and assert success
        CommandResult result = tagCommand.execute(model);

        assertEquals(String.format(RoleCommand.MESSAGE_ADD_PERSON_ROLE_SUCCESS, Messages.format(expectedPerson)),
                result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_tagAlreadyExists_throwsCommandException() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());

        Person personToTag = model.getFilteredPersonList().get(0);

        Name name = new Name(VALID_NAME_AMY);
        RoleCommand.PersonWithRoleDescriptor descriptor = new PersonWithRoleDescriptorBuilder().withRole(VALID_TAG_FRIEND).build();

        RoleCommand tagCommand = new RoleCommand(indexLastPerson, null, descriptor);

        assertThrows(CommandException.class, () -> tagCommand.execute(model),
                String.format(RoleCommand.MESSAGE_DUPLICATE_ROLE, personToTag.getName()));
    }

    @Test
    public void equals() {
        RoleCommand tagCommand1 = new RoleCommand(INDEX_FIRST_PERSON, null,
                new PersonWithRoleDescriptorBuilder().withRole(VALID_TAG_FRIEND).build());
        RoleCommand tagCommand2 = new RoleCommand(INDEX_FIRST_PERSON, null,
                new PersonWithRoleDescriptorBuilder().withRole(VALID_TAG_FRIEND).build());

        // same object -> returns true
        assertEquals(tagCommand1, tagCommand1);

        // same values -> returns true
        assertEquals(tagCommand1, tagCommand2);

        // different types -> returns false
        assertEquals(false, tagCommand1.equals(new Object()));

        // null -> returns false
        assertEquals(false, tagCommand1.equals(null));
    }

}