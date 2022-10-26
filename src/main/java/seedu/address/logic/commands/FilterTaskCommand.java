package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;

import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.Model;
import seedu.address.model.task.TaskCategory;
import seedu.address.model.task.TaskCategoryAndDeadlinePredicate;
import seedu.address.model.task.TaskContainsCategoryPredicate;
import seedu.address.model.task.TaskDeadline;
import seedu.address.model.task.TaskDeadlineBeforeDatePredicate;

/**
 * Filters and lists all tasks in address book with category that matches the argument category
 * and / or with deadline that matches the argument date.
 */
public class FilterTaskCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters all tasks based on the user input "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: "
            + "[" + PREFIX_CATEGORY + "CATEGORY (database/frontend/backend/uiux/presentation/others)] "
            + "[" + PREFIX_DEADLINE + "DATE (YYYY-MM-DD)]\n"
            + "Requirement: At least one parameter to filter with must be provided.\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CATEGORY + "backend "
            + PREFIX_DEADLINE + "2022-12-12";

    private final FilterTaskDescriptor filterTaskDescriptor;
    private final TaskCategoryAndDeadlinePredicate both;
    private final TaskContainsCategoryPredicate category;
    private final TaskDeadlineBeforeDatePredicate date;
    private final boolean hasCategory;
    private final boolean hasDate;

    /**
     * @param filterTaskDescriptor details to filter the list of tasks with
     */
    public FilterTaskCommand(FilterTaskDescriptor filterTaskDescriptor) {
        requireNonNull(filterTaskDescriptor);
        this.filterTaskDescriptor = new FilterTaskDescriptor(filterTaskDescriptor);

        if (filterTaskDescriptor.getCategory().isPresent() && filterTaskDescriptor.getDate().isPresent()) {

            both = new TaskCategoryAndDeadlinePredicate(
                    filterTaskDescriptor.getCategory().get(), filterTaskDescriptor.getDate().get());
            category = null;
            date = null;

            hasCategory = true;
            hasDate = true;

        } else if (filterTaskDescriptor.getCategory().isPresent() && filterTaskDescriptor.getDate().isEmpty()) {

            both = null;
            category = new TaskContainsCategoryPredicate(filterTaskDescriptor.getCategory().get());
            date = null;

            hasCategory = true;
            hasDate = false;

        } else {

            both = null;
            category = null;
            date = new TaskDeadlineBeforeDatePredicate(filterTaskDescriptor.getDate().get());

            hasCategory = false;
            hasDate = true;

        }
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        if (hasCategory && hasDate) {
            model.updateFilteredTaskList(both);
        }

        if (hasCategory && !hasDate) {
            model.updateFilteredTaskList(category);
        }

        if (!hasCategory && hasDate) {
            model.updateFilteredTaskList(date);
        }

        return new CommandResult(
                String.format(Messages.MESSAGE_TASK_LISTED_OVERVIEW, model.getFilteredTaskList().size()));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterTaskCommand)) {
            return false;
        }

        // state check
        FilterTaskCommand f = (FilterTaskCommand) other;
        return filterTaskDescriptor.equals(f.filterTaskDescriptor);
    }

    /**
     * Stores the details to filter the task list with.
     * Each non-empty field value will contribute to the corresponding task fields for filtering.
     */
    public static class FilterTaskDescriptor {
        private TaskCategory category;
        private TaskDeadline date;

        public FilterTaskDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public FilterTaskDescriptor(FilterTaskDescriptor toCopy) {
            setCategory(toCopy.category);
            setDate(toCopy.date);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(category, date);
        }

        public Optional<TaskCategory> getCategory() {
            return Optional.ofNullable(category);
        }

        public void setCategory(TaskCategory category) {
            this.category = category;
        }

        public Optional<TaskDeadline> getDate() {
            return Optional.ofNullable(date);
        }

        public void setDate(TaskDeadline date) {
            this.date = date;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof FilterTaskDescriptor)) {
                return false;
            }

            // state check
            FilterTaskDescriptor f = (FilterTaskDescriptor) other;
            return getCategory().equals(f.getCategory()) && getDate().equals(f.getDate());
        }
    }
}
