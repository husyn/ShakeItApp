curl -H "Authorization: Bearer 009b9af198f3a728cd38b691314e20e166c9a2c2" "https://api.buildkite.com/v2/organizations/husyn/pipelines/shakeit/builds"   -X "POST"   -F "commit=HEAD"   -F "branch=master"   -F "message=First build :rocket:"