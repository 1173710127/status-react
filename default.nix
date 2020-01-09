{
  config ? { }, # for passing status_go.src_override and status_go.enable_nimbus
}:

let
  main = import ./nix/default.nix { inherit config; };
in {
  inherit (main) pkgs targets shells;
}
