VotingRules
===========

Compute winners in elections based on various voting rules.


External Dependencies
=====================

To compute Dodgson scores via the CPLEX integer programming solver, you will need to link [cplex.jar](http://www-01.ibm.com/software/commerce/optimization/cplex-optimizer/).  For those in Academia, IBM plays nicely and offers a very free academic license; for those outside of Academia, there's a 90-day free trial available on their website.

Internal Dependencies
=====================

We expect to load [Preflib](http://www.preflib.org/) data, and use the [OpenCSV](http://opencsv.sourceforge.net/) Java library (along with custom loader code) to do this.  This .jar file is included in the repository.
