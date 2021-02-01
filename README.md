# Scarpet App Tester

This is a mod thought to automatically test multiple events, commands and functions from Scarpet Apps, in order to test for problems 
before they arise. It will be included in a Github Action when finished.

## Current Status

Currently this mod has the boilerplate to support tests that will implement an interface in order to every test follow the same call structure at the end.
None, except for the Scarpet Generated Test is implemented, although that one still doesn't have any way to create test subjects or even load the apps to manage those
tests.

## Setup

In order to run this in your apps, you have two options:

- Wait for me to have time and will to make it at least work
- Implement some of the tests yourself, make a PR, and wait for more instructions

If you want to setup this in the IDE, it works like any other Fabric mod. Basically clone the repo and do `gradlew eclipse` (or import into IntellIj if you use that), and
the project should automatically prepare the dependencies (Carpet and Minecraft) in order to be able to build, compile and test.

## Scarpet Docs

Even though this isn't ready by far, some docs for Test checker apps are already available, you can check those [here](docs/ScarpetDocs.md). 
