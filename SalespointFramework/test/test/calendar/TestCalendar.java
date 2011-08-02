package test.calendar;

import javax.persistence.EntityManager;

import org.salespointframework.core.calendar.AbstractCalendar;

public class TestCalendar extends AbstractCalendar<TestEntry> {

    public TestCalendar(EntityManager em) {
        super(em);
    }
    
    @Override
    public Class<TestEntry> getContentClass() {
        return TestEntry.class;
    }

}
