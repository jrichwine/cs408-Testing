# cs408-Testing

- Kinshuk Juneja
- Nadeem Mahmood
- Jeremiah Murphy
- Michael Reed
- Jacob Richwine
- Zhandos Suleimenov

##Problem statement:
As the enrollment at Purdue continues to increase, it is becoming harder to find a
noncrowded
place to meet with your group, study for classes, or a dining court that isn't
packed. Existing programs only cover labs or other subsets of buildings; ours will encompass
multiple categories of buildings. Our program, the BoilerCheck Android app, will allow users to
checkin
and report conditions at popular buildings around campus. Users will also be able to
view current statistics of the building before making a decision on where to go.

##Backend Setup
- Install MongoDB
- Create folder on C: for db  example “c:/testingdb”
- Navigate to mongo/bin folder
- Run mongod –dbpath “c:/testingdb”
- You should now see files populated in c:/testingdb
- Install node
- Clone the branch
- from command line type: node index
- You should see Middle-tier listening on port 3000
- You can now go to localhost:3000 and see a message