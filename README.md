# dynamic-dns-updater

Updates a Route53 record based on the current (external) IP address (if it needs updating).

## Notes

* Expects AWS credentials in the profile `rik-systems-dynamic-dns-updater` (should be created by [rikbrown.co.uk](https://github.com/rikbrown/rikbrown.co.uk)
  Cloudformation and put into `.aws/credentials`).
* Currently hardcoded to support `seattle.rik.systems` record in hosted zone `Z39HKRRJQ8AJ0`.

