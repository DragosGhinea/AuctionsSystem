# AuctionsSystem | Stage 1

In this stage some actions and interrogations of the project should be defined.
Minimum 8 class types should be defined as well.

After defining comes the implementation which should contain:
  - private/protected types and access methods
  - minimum 2 collection types including at least a sorted collection
  - inheritance
  - a service class which exposes the system's operations
  - a Main class in which access to the services will be done

## How the system works


### Users

From the main terminal you have an option to either login or create an account.
After creating/logging with a user account a new "session console" will open.
You can open multiple user sessions from the main terminal.

A user should have details such as:
 - email
 - username
 - passwordHash
 - born date
 - first name
 - last name

### Auctions

Auctions are system side objects (meaning that a user can not create them)
because our auction house prides itself with the authenticity of the items  presented.
We do background checks, sign safety contracts with the items' providers and so on.
We manage the auction registration itself somewhere else and just create
it in the system via APIs after.

There are be two types of auctions:
 - LongAuction
 - BlitzAuction

A **LongAuction** is an auction that is created for a specified period of time (eg. 5 days)
after which the highest bid wins.

A **BlitzAuction** is a short auction that doesn't take more than one hour.
It is more intense than a long auction because people have a very short amount of time
to bid after the bidding has started (eg. 10 seconds), and the time refreshes after each bid.

There are systems to prevent exploits such as:
 - for long auctions the money is taken from your account and in case of losing you get it back when the auction ends
 - for long auctions, if a bid is placed at the last moment, the auction time is extended to allow counter bids
 - for blitz auctions the money is not taken from your account until the end, but if you don't have the amount of money to pay your highest bid, the win goes to the next valid bid
 - for blitz auctions if you reach 3 invalid auctions (by trying the exploit above), your account gets suspended


### Wallets

Each user should have a wallet from which money will be subtracted for auctions.
For simplicity, when you deposit money into the wallet, your money will get converted into
a special system currency named **credits**.

Although all the auctions will be held in points, if you select a preferred currency,
the auctions will be displayed in that currency for you.

**IMPORTANT** For the sake of simplicity, once more, we will assume a fixed convert rate for all registered currencies.

### Bidding

A **bid** should hold identification information about:
 - the auction it belongs to
(we will handle that via a bridge between a bid and an auction, to be more precise a **BidHistory**).
 - the user that has placed the bid
(for the user we'd also like to know the auctions to which they bid)
 - the amount of credits promised

### Auction States and Time Manager

An auction can be in a various number of states but some basic ones we consider:
 - NotStarted
 - Ongoing
 - Ended
 - Cancelled
 - Overtime (specific for LongAuction)
 - Preparing (specific for BlitzAuction)

These states should be controlled automatically by an internal **AuctionStateManager** which manages both the state and the time options of an auction.

### Rewards

You win an auction but do you actually win? A reward of course!

Rewards can be:
 - SingleReward (a single item is considered a reward)
 - RewardPack (a list of items is considered a reward)

In blitz auctions, to give more people a chance and to make it last longer, you can make a **RewardPack** be auctioned
as a series of single rewards, one right after another.

### Auction Browser

Users must have a way to check the available auctions so a browser class
will provide a list to them, with either sorted or filtered auctions.

### AuctionContainer

To properly display changes of the auction in real time, we need an observer
to tell the user sessions that are checking that auction out what modifications
are being made to the auction.

### Display

How is the information displayed to the users? We need a class for that.
For the first stage we will implement a "TerminalDisplay" but in the second stage
a prettier display may be implemented.


