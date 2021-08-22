# Hash Collisions

A hashing library was used called HashCodeBuilder which allowed for an easier time creating and checking hashcodes.

Hash codes were used for ScheduleNode by overriding the equals and hashCode methods. This allowed to remove duplicate schedules that would otherwise be added to the open list priority queue and therefore saved a lot of memory. This was achieved by keeping a list of all unique created schedules and checking against that list whether a new child schedule is unique or not through checking if their hashCodes() are the same.

Equal schedules will always return the same hashcode but it is also possible that two different schedules return the same hashcode therefore would end up being equal when they are not. This is an issue as it could result in schedules not being added to the open list due to a different schedule having the same hashcode that was created before it. 

However without using hashing we were not able to achieve the memory limits and therefore decided to use it despite its problems.