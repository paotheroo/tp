package seedu.address.model.task;



public class TaskCategory {
    private int level;
    private TaskCategoryType taskCategoryType;

    public TaskCategory(int level, TaskCategoryType taskCategoryType){
        this.level = level;
        this.taskCategoryType = taskCategoryType;
    }

    /**
     * Returns the category of a task.
     *
     * @return The task category.
     */
    public TaskCategoryType getTaskCategoryType() {
        return this.taskCategoryType;
    }

    /**
     * Returns the level of a task.
     *
     * @return The task level.
     */
    public int getLevel() {
        return this.level;
    }


    /**
     * Returns hashcode of the current object
     *
     * @return Hashcode of the object.
     */
    @Override
    public int hashCode() {
        return taskCategoryType.hashCode();
    }


    /**
     * Returns the string representation of the task category.
     *
     * @return Task priority.
     */
    @Override
    public String toString() {
        return level + taskCategoryType.name();
    }

    /**
     * Compares another object with the Category object.
     *
     * @param other The other object to be compared to.
     * @return If the two objects are equal.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskCategory // instanceof handles nulls
                && ((TaskCategory) other).level == this.level
                && ((TaskCategory) other).taskCategoryType.equals(this.taskCategoryType)
        );
    }

}