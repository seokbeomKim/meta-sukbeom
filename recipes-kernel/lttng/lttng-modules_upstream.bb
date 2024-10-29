SECTION = "devel"
SUMMARY = "Linux Trace Toolkit KERNEL MODULE"
DESCRIPTION = "The lttng-modules 2.0 package contains the kernel tracer modules"
HOMEPAGE = "https://lttng.org/"
LICENSE = "LGPL-2.1-only & GPL-2.0-only & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=018e002dbdda3306682e394ddd65fa32"

inherit module

include lttng-platforms.inc

SRC_URI = "git://seokbeomKim@review.lttng.org:29418/lttng-modules;branch=master;protocol=ssh"
SRCREV = "166a05ce64a05218f51563d201644ab3bfddaacc"

S = "${WORKDIR}/git"

# Use :append here so that the patch is applied also when using devupstream
SRC_URI:append = " \
"

SRC_URI[sha256sum] = "297211d6fda459c85793c1f498c90fad2939cda7939d503f3ec5eaaf5fbec3c7"

export INSTALL_MOD_DIR="kernel/lttng-modules"

EXTRA_OEMAKE += "KERNELDIR='${STAGING_KERNEL_DIR}'"

MODULES_MODULE_SYMVERS_LOCATION = "src"

do_install:append() {
	# Delete empty directories to avoid QA failures if no modules were built
	if [ -d ${D}/${nonarch_base_libdir} ]; then
		find ${D}/${nonarch_base_libdir} -depth -type d -empty -exec rmdir {} \;
	fi
}

python do_package:prepend() {
    if not os.path.exists(os.path.join(d.getVar('D'), d.getVar('nonarch_base_libdir')[1:], 'modules')):
        bb.warn("%s: no modules were created; this may be due to CONFIG_TRACEPOINTS not being enabled in your kernel." % d.getVar('PN'))
}

BBCLASSEXTEND = "devupstream:target"
SRC_URI:class-devupstream = "git://git.lttng.org/lttng-modules;branch=master;protocol=https"
SRCREV:class-devupstream = "${AUTOREV}"
SRCREV_FORMAT ?= "lttng_git"
