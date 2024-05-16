package com.os.frontend.scheduling_window.observers;

import com.os.backend.main.SystemScheduler;

public interface Observer {
    void update(SystemScheduler systemScheduler);
}
