package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.task.Status;
import seedu.address.model.task.Task;

/**
 * Marks a task as completed.
 */
public class MarkCommand extends Command {
    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task identified by the index number used in the displayed task list as completed.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_TASK_SUCCESS = "Task Completed: %1$s";
    public static final String MESSAGE_MARK_MARKED_TASK = "The task is already marked";

    private final Index targetIndex;

    public MarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToMark = lastShownList.get(targetIndex.getZeroBased());

        Status status = taskToMark.getStatus();
        if (status.isCompleted()) {
            throw new CommandException(MESSAGE_MARK_MARKED_TASK);
        }

        if (taskToMark.getDeadline() == null) {
            Task markedTask = new Task(taskToMark.getDescription(), status.updateStatus());
            model.setTask(taskToMark, markedTask);
        } else {
            Task markedTask = new Task(taskToMark.getDescription(), status.updateStatus(), taskToMark.getDeadline());
            model.setTask(taskToMark, markedTask);
        }

        return new CommandResult(String.format(MESSAGE_MARK_TASK_SUCCESS, Messages.format(taskToMark)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherMarkCommand = (MarkCommand) other;
        return targetIndex.equals(otherMarkCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
