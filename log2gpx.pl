#!/usr/bin/perl
use strict;

use LWP::UserAgent;
use Storable;

my $bssid_cache = {};
my $cacheFile = '/tmp/bssidCache';
$| = 1;

if (-f $cacheFile) {
  $bssid_cache = retrieve($cacheFile);
}

while (<>) {
  chomp;
  my ($time, $log) = split(/\t/, $_);
  my ($ts, $ymd, $his, $cst, $loc) = split(/ /, $time);
  my ($label, $bssid, $signal, $ssid) = split(/ /, $log);
}

sub bssidLookup {
  my ($bssid) = @_;

  return $bssid_cache->{$bssid} if($bssid_cache->{$bssid});

  my $latlon = getLatLonByBssid($bssid);
  $bssid_cache->{$bssid} = $latlon if ($latlon);

  return $latlon;
}

sub getLatLonByBssid {
  my ($bssid) = @_;
  my $ua = new LWP::UserAgent(
    timeout => 10,
    agent => 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1',
  );
}
