const { Octokit } = require('@octokit/rest');
async function run() {

  const octokit = new Octokit({ auth: process.env.GITHUB_TOKEN });

  // Get information about the current pull request
  const { data: pullRequest } = await octokit.pulls.get({
    owner: process.env.GITHUB_REPOSITORY.split('/')[0],
    repo: process.env.GITHUB_REPOSITORY.split('/')[1],
    pull_number: process.env.PR_NUMBER
  });
    console.log(process.env.GITHUB_TOKEN)
    console.log(process.env.GITHUB_REPOSITORY.split('/')[0])
    console.log(process.env.GITHUB_REPOSITORY.split('/')[1])
    console.log(process.env.GITHUB_PULL_REQUEST_NUMBER)

    console.log(pullRequest)

    const headSha = pullRequest.head && pullRequest.head.sha;

  const annotations = [
    {
      path: '.github/scripts/annotate-code.js',
      start_line: 13,
      end_line: 13,
      annotation_level: 'failure',
      message: 'This is an annotation message',
    },
  ];
    console.log("after annotations definition")
  // Create a check run
  const checkRun = await octokit.checks.create({
    owner: process.env.GITHUB_REPOSITORY.split('/')[0],
    repo: process.env.GITHUB_REPOSITORY.split('/')[1],
    name: 'Annotate Code',
    head_sha: pullRequest.head.sha,
    status: 'completed',
    conclusion: 'success', // or 'failure'
    output: {
      title: 'Code Annotations',
      summary: 'Summary of code annotations',
      annotations,
    },
  });

  console.log(`Annotations added. Check run created: ${checkRun.data.html_url}`);
}

run().catch((error) => {
  console.error(error);
  process.exit(1);
});