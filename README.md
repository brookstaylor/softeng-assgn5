# softeng-assgn5

This project will focus on making GME more accessible to Vanderbilt students involved in Greek Life. GME is a credit program started by the Office of Greek Life that every member must complete in order to avoid putting their chapter on social suspension. It consists of 6 tracks, healthy behaviors, community impact, campus involvement, diversity and inclusion, personal development, and faculty engagement, and each track has 2 credits that must be completed. There is a 13th credit that is received by completing 13 hours of community service.

Currently, students hear about GME through weekly emails from the Director of Greek Life, Kristin Torrey, announcements at their chapter meetings, or event listings on Anchorlink. These methods either take time and effort from individuals who could better use that time (chapter presidents making announcements at chapter, Kristin compiling email lists) or use a system that is confusing and clumsy (Anchorlink). They want to complete these credits in order to avoid placing their chapter on suspension as well as in order to experience events that they might otherwise not be able to experience. It is of my opinion, and I think some of my peers, that GME could be displayed and events found in a much easier way.

The goal of this project is then to create an application using Clojure and potentially Java/Javascript to display GME events that are available during a specific time period (week or day) and eventually specify specific tracks of GME (healthy behaviors, personal development, etc.) via text messaging. A student interested in GME could text a number "GME Tuesday healthy behaviors" and would be returned an anchorlink url that led them to those events.

# Questions:
  1. Why do you care about GME?
  2. How do you find out what GME events are available to you?
  3. Why do you use the method you specified in question two to find out what GME events are available to you?
  4. Are you satisfied with how you currently find what GME events are available to you? Why or why not?
  5. Are you ever frustrated by GME? If so, what frustrates you?
  6. Are you satisfied with how you currently find which GME you need? Why or why not?
  7. Could it be easier to finish GME? How?
  8. Do you think being able to receive GME events via text message would help you?
  9. What was your favorite (or least favorite) GME you've been to and why?
  10. Do you hear about events you wish you could have gone to but didn't know about?


# Answers:

## Question 1: 
  1. I care about GME because my chapter will get in trouble from the school if it is ignored.
  2. I would like to make sure that my house stay on campus and that we’re able to have events. I also would like to maintain the spirit of philanthropy amongst greek life. 
  3. It is a way for the Greek community to get outside of its bubble and engage with the Vanderbilt community at large. 

## Question 2:
  1. Through my fraternity and email.
  2. Chapter announcements.
  3. Chapter announcements.

## Question 3:
  1. Because my fraternity takes charge in making sure its members know what evens occur when.
  2. Easiest method without doing too much searching on a terrible system like AnchorLink
  3. We have found it best to have one person cull the events that will be easist for our chapter to make it to. 

## Question 4:
  1. I am satisfied because I would not go to more events if I knew about them.
  2. No because it’s easy to forget a verbal announcement. AnchorLink is also not an ideal tool because its very cumbersome and oftentimes does not properly identify which events count for GME and which ones do not. 
  3. No, I think that we could do a better job of getting notified a few hours before specific events rather than just once at chapter. 

## Question 5:
  1. I am frustrated by GME because I do not find it necessary or adding anything to my college experience.
  2. Yes. I am most frustrated by how inorganic it is. It is quite apparent at event that count for GME that people are not engaged and often just swipe into the event and then leave immediately after. I think if there was a better way to find GME events people are actually excited about it would help change this behavior.
  3. Yes. I think it often makes pushes fake involvement, especially in the Diversity and Inclusion section. I don't think people actually care about expanding their horizons, and GME makes it easy for them to pretend they do. 

## Question 6:
  1. I am satisfied using Anchorlink.
  2. No. I want to find event I am actually excited about and I want it to be clearly laid out, but currently things are very unorganized and oftentimes I just go to whatever events I find out about because I just try to get it over with.
  3. I think that the current method feels too nebulous and unfocused.

## Question 7:
  1. It could be easier to finish GME if requirements were less stringent.
  2. Yes if we were more aware of when events were and how they fit in with our schedule.
  3. Yes. There could be better advertising of events.

## Question 8:
  1. That may help if I could sign up for texts regarding GME that interest me.
  2. Yes, it would be nice to be able to text a bot and it tell me the events and categories they fulfill during a specified time window
  3. Definitely

## Question 9:
  1. My favorite GME that I have been to are the events in the Wondry because I find them informative and useful.
  2. An event we had at the house today in support of one of my friends that recently beat cancer. It was a nice celebration with many friends and familiar faces.
  3. The guided meditation at the Student Wellness Center was great. 

## Question 10:
  1. Yes I have heard about events that seem interesting that I did not know about before.
  2. Yes always after the fact. I rarely hear about cool GME events before they happen. Oftentimes people will just have come back from an event that they thought was a lot of fun but I had no idea about.
  3. Sometimes yes. 
  
# Requirements
  1. Information is able to be conveyed via text message (keep in mind the size/amount of info we are sending to the user)
  2. Able to grab current date and search a url based on the date
  3. Events for a week and a specific date will be able to be displayed
  4. Events for a certain track of GME will be able to be displayed
  5. Formatting of event information will be convenient and easy to read

# Development Approach
  The work that we did in assignments 1-3 should be easily modifiable to fit the specs of the new application. I am first going to work on deciphering the URL for anchorlink based on different filters for specific GME events and dates. Once I am able to decipher how the URL changes based on different parameters, I should be able to hard code that URL into the application that the user can then query based on their desired info. The most important development decisions will be how I structure the map that I'm going to use to store the events. Right now I'm thinking that I will have a map with the structure GME --> date --> track --> url that I can then pull based on the user query. I should be able to fairly easily adapt the functions we've already implemented as part of the previous assignments, specifically using the experts-register function and the ask-experts function, except instead of an experts and a messages I'm going to be dealing with URLs. The thought behind returning a URL to the user instead of storing the events in the map itself is that it's less storage space (anchorlink is already storing it once why should we duplicate it) and it still does all the work for the user (removes the manual selecting of parameters and logging into anchorlink, plus its accessible from a phone). Overall, I'm optimistic this project can be completed within the proposed timeline and that it will provide tangible benefit to the user.
  
# Documentation and Supported Applications
  The app can be run by texting the number +15134492523. It supports the commands 'hi', 'more', and 'what'. 'hi' can also be called by 'yo', 'hey', 'hello', or 'welcome' and prints out a welcome message along with a list of valid commands and their applications. 'more' sends an informative paragraph letting the user know the specifics of GME, how to complete it, and how to track their progress. 'what' allows the user to query for a specific track of GME anytime or tomorrow and returns a link to anchorlink that displays the specified events. It follows the format 'what [gme/diversity-inclusion/community-impact/faculty-engagement/campus-involvement/personal-development/healthy-behaviors] [anytime/tomorrow]'.
