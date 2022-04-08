# Development Process Workflow

## 1. Make a new feature branch from `main`
```
git fetch
git checkout -b <new_branch_name> origin/main
git push -u origin HEAD
```
- Replace `<new_branch_name>` with the name of your branch. Your branch name should be short but descriptive (e.g., `refactor-authentication`, `user-content-cache-key`, `make-retina-avatars`) so that others can see what is being worked on.
- `push -u` is an easy way to push the branch to the remote repository and get the "upstream tracking" set up correctly.
- Remember to move the Trello card to "Doing".

## 2. Implement feature
- Remember to commit & push often as you make changes and work on the feature.
- If others are also working on the same branch as you, you might need to pull their changes using `git pull` (or `git pull --rebase`).
- If other PRs are merged into `main` before yours, you will need to pull the new changes from `main` using `git pull origin main`.

## 3. Create pull request
Go to our repository page, select your branch, then select "Compare & pull request".
1. Make sure the base branch is `main` (this means your work will be merged into `main`).
2. Add a title (what feature did you work on?), and a description if you have details to add.
3. If your code is ready for review, click "Create Pull Request". If you are not done working on it, use the dropdown and select "Create Draft Pull Request" (once you are done, you can mark it as ready for review).
- Remember to move the Trello card to "Code Review"

## 4. Review process
1. Each PR need at least 2 review (another dev does an overview of your code to make sure there are no major issues/things missing).
2. The reviewer(s) may pull the branch locally and try running the app to verify the functionality.
   1. If found issues, they will tell the developer via GitHub review comments.
   2. If tester has not found issues, they will approve the request
3. The request can be merged (by anyone) once it has 2 reviews. Once merged, move the Trello card to "Done"
4. Good job!
