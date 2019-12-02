# RemedyStuff
Utilities and scripts I have written for use for use with BMC Remedy Action Request System (ARS)

ATTStoreCleanService.ps1: Modified original sample service for use as a cleaner for the BMC ARS MidTier ATTStore directory that can fill up when users 'X' out of their browser rather than gracefully logging out. Naturally this can causes problems from filling up the disk potentially causing Tomcat to crash to merely not allowing user to upload any more attachments. Worse case if tomcat were installed on Windows C: drive (Nobody does that right?) it could cause the server or VM to lock up if not crash.

ATTStoreClean.ps1: Same function as the service above but just the bear bones script to preform the action, modify to meet your instalation and schedule to run as needed.
