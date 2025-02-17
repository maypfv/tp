package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyTaskWise;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.TaskWise;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;
import seedu.address.ui.MainWindow;

public class AddCommandTest {

    private AddCommand.AddTaskDescriptor desc = new AddCommand.AddTaskDescriptor();

    private void setUpDesc(Task validTask) {
        desc.setDescription(validTask.getDescription());
        desc.setDeadline(validTask.getDeadline());
        desc.setPriority(validTask.getPriority());
        desc.setMembers(validTask.getMembers());
    }
    @AfterEach
    public void reset() {
        desc = new AddCommand.AddTaskDescriptor();
    }

    @Test
    public void constructor_nullTask_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_taskAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTaskAdded modelStub = new ModelStubAcceptingTaskAdded();
        Task validTask = new TaskBuilder().build();

        setUpDesc(validTask);

        CommandResult commandResult = new AddCommand(desc).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validTask)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validTask), modelStub.tasksAdded);
    }

    @Test
    public void execute_duplicateTask_throwsCommandException() {
        Task validTask = new TaskBuilder().build();

        setUpDesc(validTask);
        AddCommand addCommand = new AddCommand(desc);
        ModelStub modelStub = new ModelStubWithTask(validTask);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_TASK, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Task userGuide = new TaskBuilder().withDescription("Finish User Guide").build();
        Task developerGuide = new TaskBuilder().withDescription("Finish Developer Guide").build();

        AddCommand.AddTaskDescriptor desc2 = new AddCommand.AddTaskDescriptor();

        desc2.setDescription(developerGuide.getDescription());
        desc2.setDeadline(developerGuide.getDeadline());
        desc2.setPriority(developerGuide.getPriority());
        desc2.setMembers(developerGuide.getMembers());

        setUpDesc(userGuide);
        AddCommand addUserGuideCommand = new AddCommand(desc);
        AddCommand addDeveloperGuideCommand = new AddCommand(desc2);

        // same object -> returns true
        assertTrue(addUserGuideCommand.equals(addUserGuideCommand));

        // same values -> returns true
        AddCommand addUserGuideCommandCopy = new AddCommand(desc);
        assertTrue(addUserGuideCommand.equals(addUserGuideCommandCopy));

        // different types -> returns false
        assertFalse(addUserGuideCommand.equals(1));

        // null -> returns false
        assertFalse(addUserGuideCommand.equals(null));

        // different task -> returns false
        assertFalse(addUserGuideCommand.equals(addDeveloperGuideCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(desc);
        String expected = AddCommand.class.getCanonicalName() + "{desc=" + addCommand.getDesc() + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getTaskWiseFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTaskWiseFilePath(Path taskWiseFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTaskWise(ReadOnlyTaskWise newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyTaskWise getTaskWise() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTask(Task task) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTask(Task target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTask(Task target, Task editedTask) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAllTasks(List<Task> tasks) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Task> getFilteredTaskList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTaskList(Predicate<Task> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public MainWindow getMainWindow() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setMainWindow(MainWindow mainWindow) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single task.
     */
    private class ModelStubWithTask extends ModelStub {
        private final Task task;

        ModelStubWithTask(Task task) {
            requireNonNull(task);
            this.task = task;
        }

        @Override
        public boolean hasTask(Task task) {
            requireNonNull(task);
            return this.task.isSameTask(task);
        }
    }

    /**
     * A Model stub that always accept the task being added.
     */
    private class ModelStubAcceptingTaskAdded extends ModelStub {
        final ArrayList<Task> tasksAdded = new ArrayList<>();

        @Override
        public boolean hasTask(Task task) {
            requireNonNull(task);
            return tasksAdded.stream().anyMatch(task::isSameTask);
        }

        @Override
        public void addTask(Task task) {
            requireNonNull(task);
            tasksAdded.add(task);
        }

        @Override
        public ReadOnlyTaskWise getTaskWise() {
            return new TaskWise();
        }
    }

}
