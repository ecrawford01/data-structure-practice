# Duplicate Tracker
The purpose of this class is to process a sequence of user IDs such that duplicate IDs are identified and may be efficiently retrieved as a sorted List. 

# Job Sequencer
The purpose of this class is to process jobs of different types in the order that they are added to the system. Because there are a limited number of job handlers and they are specialized for a particular job type, "nextJob" methods only return jobs of the requested type. The jobs of a particular type are processed in the order in which they arrived. Both the "addJob" and the "nextJob" methods may be called arbitrarily many times and in any order.

# Event Sequencer
The purpose of this class is to store events that are tagged with an integer time stamp and then to return those events in strict chronological order: once an event with a particular time stamp has been retrieved, it should not be possible to retrieve an event with an equal or earlier time stamp, even if this means that some events are discarded. Assume that both the "add" and the "get" methods may be called arbitrarily many times and in any order: efficiency matters for both.

# Triage
The purpose of this class is to prioritize patients as they seek health care. Each patient is entered into the system along with an integer priority value that quantifies the urgency of their medical condition. The nextPatient method should always remove and return the patient with the highest priority. In event of tied priorities, patients should be processed in the order they were added. The removePatient method makes it possible to remove a patient from the collection even if he or she has not been served.
