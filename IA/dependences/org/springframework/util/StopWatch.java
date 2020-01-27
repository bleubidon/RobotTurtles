// 
// Decompiled by Procyon v0.5.36
// 

package org.springframework.util;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

public class StopWatch
{
    private final String id;
    private boolean keepTaskList;
    private final List<TaskInfo> taskList;
    private long startTimeMillis;
    private boolean running;
    private String currentTaskName;
    private TaskInfo lastTaskInfo;
    private int taskCount;
    private long totalTimeMillis;
    
    public StopWatch() {
        this.keepTaskList = true;
        this.taskList = new LinkedList<TaskInfo>();
        this.id = "";
    }
    
    public StopWatch(final String id) {
        this.keepTaskList = true;
        this.taskList = new LinkedList<TaskInfo>();
        this.id = id;
    }
    
    public void setKeepTaskList(final boolean keepTaskList) {
        this.keepTaskList = keepTaskList;
    }
    
    public void start() throws IllegalStateException {
        this.start("");
    }
    
    public void start(final String taskName) throws IllegalStateException {
        if (this.running) {
            throw new IllegalStateException("Can't start StopWatch: it's already running");
        }
        this.startTimeMillis = System.currentTimeMillis();
        this.running = true;
        this.currentTaskName = taskName;
    }
    
    public void stop() throws IllegalStateException {
        if (!this.running) {
            throw new IllegalStateException("Can't stop StopWatch: it's not running");
        }
        final long lastTime = System.currentTimeMillis() - this.startTimeMillis;
        this.totalTimeMillis += lastTime;
        this.lastTaskInfo = new TaskInfo(this.currentTaskName, lastTime);
        if (this.keepTaskList) {
            this.taskList.add(this.lastTaskInfo);
        }
        ++this.taskCount;
        this.running = false;
        this.currentTaskName = null;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public long getLastTaskTimeMillis() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task interval");
        }
        return this.lastTaskInfo.getTimeMillis();
    }
    
    public String getLastTaskName() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task name");
        }
        return this.lastTaskInfo.getTaskName();
    }
    
    public TaskInfo getLastTaskInfo() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task info");
        }
        return this.lastTaskInfo;
    }
    
    public long getTotalTimeMillis() {
        return this.totalTimeMillis;
    }
    
    public double getTotalTimeSeconds() {
        return this.totalTimeMillis / 1000.0;
    }
    
    public int getTaskCount() {
        return this.taskCount;
    }
    
    public TaskInfo[] getTaskInfo() {
        if (!this.keepTaskList) {
            throw new UnsupportedOperationException("Task info is not being kept!");
        }
        return this.taskList.toArray(new TaskInfo[this.taskList.size()]);
    }
    
    public String shortSummary() {
        return "StopWatch '" + this.id + "': running time (millis) = " + this.getTotalTimeMillis();
    }
    
    public String prettyPrint() {
        final StringBuilder sb = new StringBuilder(this.shortSummary());
        sb.append('\n');
        if (!this.keepTaskList) {
            sb.append("No task info kept");
        }
        else {
            sb.append("-----------------------------------------\n");
            sb.append("ms     %     Task name\n");
            sb.append("-----------------------------------------\n");
            final NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(5);
            nf.setGroupingUsed(false);
            final NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(3);
            pf.setGroupingUsed(false);
            for (final TaskInfo task : this.getTaskInfo()) {
                sb.append(nf.format(task.getTimeMillis())).append("  ");
                sb.append(pf.format(task.getTimeSeconds() / this.getTotalTimeSeconds())).append("  ");
                sb.append(task.getTaskName()).append("\n");
            }
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.shortSummary());
        if (this.keepTaskList) {
            for (final TaskInfo task : this.getTaskInfo()) {
                sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeMillis());
                final long percent = Math.round(100.0 * task.getTimeSeconds() / this.getTotalTimeSeconds());
                sb.append(" = ").append(percent).append("%");
            }
        }
        else {
            sb.append("; no task info kept");
        }
        return sb.toString();
    }
    
    public static final class TaskInfo
    {
        private final String taskName;
        private final long timeMillis;
        
        TaskInfo(final String taskName, final long timeMillis) {
            this.taskName = taskName;
            this.timeMillis = timeMillis;
        }
        
        public String getTaskName() {
            return this.taskName;
        }
        
        public long getTimeMillis() {
            return this.timeMillis;
        }
        
        public double getTimeSeconds() {
            return this.timeMillis / 1000.0;
        }
    }
}
