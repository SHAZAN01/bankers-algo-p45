# banker's algorithm project
https://github.com/SHAZAN01/bankers-algo-p45
team: shazan ansar mohammed & saina zardosht

## about this project
this is a java program that simulates the banker's algorithm to manage resources and prevent deadlocks in an operating system. we combined both of our files and work into this one final project. it reads max resources from a max.txt file and takes initial available resources from the command line.

## how to run it
compile the code:
javac Banker.java

run the code with starting resources (example for 4 resources):
java Banker 6 6 7 5

## commands
- RQ <customer number> <resources...> (requests resources)
- RL <customer number> <resources...> (releases resources)
- * (prints the available, max, allocation, and need matrices)
- quit (exits the program safely)

## test results / screenshots
we have included 6 screenshots to prove our code works:
1. 01-initial-state.png: shows program startup and reading max.txt.
2. 02-safe-request.png: shows a valid request being granted.
3. 03-unsafe-request.png: shows an unsafe request being denied.
4. 04-resource-release.png: shows releasing resources back to the pool.
5. 05-invalid-requests.png: shows the program rejecting negative requests, bad customers, and letters.
6. 06-invalid-releases.png: shows rejection of releases that exceed allocated amounts.

## who did what
we split up the work and then merged everything to make sure it was solid. 

- shazan: worked on the main loop, reading the max.txt file, the request method, and added input validation to stop crashes from bad spacing or invalid customer numbers. corrected the code from shazan and re took screenshot for the file reading and input sanitization.
- saina: worked on the safety algorithm (isSafe), the release method, and making sure the state rolls back if a request is denied so the matrices don't get messed up. corrected the code from saina and re took screenshot for the safe state checks and limits.
