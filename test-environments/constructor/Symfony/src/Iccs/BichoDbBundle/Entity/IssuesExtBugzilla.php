<?php

namespace Iccs\BichoDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\BichoDbBundle\Entity\IssuesExtBugzilla
 */
class IssuesExtBugzilla
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var string $alias
     */
    private $alias;

    /**
     * @var datetime $deltaTs
     */
    private $deltaTs;

    /**
     * @var string $reporterAccessible
     */
    private $reporterAccessible;

    /**
     * @var string $cclistAccessible
     */
    private $cclistAccessible;

    /**
     * @var string $classificationId
     */
    private $classificationId;

    /**
     * @var string $classification
     */
    private $classification;

    /**
     * @var string $product
     */
    private $product;

    /**
     * @var string $component
     */
    private $component;

    /**
     * @var string $version
     */
    private $version;

    /**
     * @var string $repPlatform
     */
    private $repPlatform;

    /**
     * @var string $opSys
     */
    private $opSys;

    /**
     * @var integer $dupId
     */
    private $dupId;

    /**
     * @var string $bugFileLoc
     */
    private $bugFileLoc;

    /**
     * @var string $statusWhiteboard
     */
    private $statusWhiteboard;

    /**
     * @var string $targetMilestone
     */
    private $targetMilestone;

    /**
     * @var integer $votes
     */
    private $votes;

    /**
     * @var string $everconfirmed
     */
    private $everconfirmed;

    /**
     * @var string $qaContact
     */
    private $qaContact;

    /**
     * @var string $estimatedTime
     */
    private $estimatedTime;

    /**
     * @var string $remainingTime
     */
    private $remainingTime;

    /**
     * @var datetime $actualTime
     */
    private $actualTime;

    /**
     * @var datetime $deadline
     */
    private $deadline;

    /**
     * @var string $keywords
     */
    private $keywords;

    /**
     * @var string $flag
     */
    private $flag;

    /**
     * @var string $cc
     */
    private $cc;

    /**
     * @var string $groupBugzilla
     */
    private $groupBugzilla;

    /**
     * @var integer $issueId
     */
    private $issueId;


    /**
     * Get id
     *
     * @return integer 
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set alias
     *
     * @param string $alias
     */
    public function setAlias($alias)
    {
        $this->alias = $alias;
    }

    /**
     * Get alias
     *
     * @return string 
     */
    public function getAlias()
    {
        return $this->alias;
    }

    /**
     * Set deltaTs
     *
     * @param datetime $deltaTs
     */
    public function setDeltaTs($deltaTs)
    {
        $this->deltaTs = $deltaTs;
    }

    /**
     * Get deltaTs
     *
     * @return datetime 
     */
    public function getDeltaTs()
    {
        return $this->deltaTs;
    }

    /**
     * Set reporterAccessible
     *
     * @param string $reporterAccessible
     */
    public function setReporterAccessible($reporterAccessible)
    {
        $this->reporterAccessible = $reporterAccessible;
    }

    /**
     * Get reporterAccessible
     *
     * @return string 
     */
    public function getReporterAccessible()
    {
        return $this->reporterAccessible;
    }

    /**
     * Set cclistAccessible
     *
     * @param string $cclistAccessible
     */
    public function setCclistAccessible($cclistAccessible)
    {
        $this->cclistAccessible = $cclistAccessible;
    }

    /**
     * Get cclistAccessible
     *
     * @return string 
     */
    public function getCclistAccessible()
    {
        return $this->cclistAccessible;
    }

    /**
     * Set classificationId
     *
     * @param string $classificationId
     */
    public function setClassificationId($classificationId)
    {
        $this->classificationId = $classificationId;
    }

    /**
     * Get classificationId
     *
     * @return string 
     */
    public function getClassificationId()
    {
        return $this->classificationId;
    }

    /**
     * Set classification
     *
     * @param string $classification
     */
    public function setClassification($classification)
    {
        $this->classification = $classification;
    }

    /**
     * Get classification
     *
     * @return string 
     */
    public function getClassification()
    {
        return $this->classification;
    }

    /**
     * Set product
     *
     * @param string $product
     */
    public function setProduct($product)
    {
        $this->product = $product;
    }

    /**
     * Get product
     *
     * @return string 
     */
    public function getProduct()
    {
        return $this->product;
    }

    /**
     * Set component
     *
     * @param string $component
     */
    public function setComponent($component)
    {
        $this->component = $component;
    }

    /**
     * Get component
     *
     * @return string 
     */
    public function getComponent()
    {
        return $this->component;
    }

    /**
     * Set version
     *
     * @param string $version
     */
    public function setVersion($version)
    {
        $this->version = $version;
    }

    /**
     * Get version
     *
     * @return string 
     */
    public function getVersion()
    {
        return $this->version;
    }

    /**
     * Set repPlatform
     *
     * @param string $repPlatform
     */
    public function setRepPlatform($repPlatform)
    {
        $this->repPlatform = $repPlatform;
    }

    /**
     * Get repPlatform
     *
     * @return string 
     */
    public function getRepPlatform()
    {
        return $this->repPlatform;
    }

    /**
     * Set opSys
     *
     * @param string $opSys
     */
    public function setOpSys($opSys)
    {
        $this->opSys = $opSys;
    }

    /**
     * Get opSys
     *
     * @return string 
     */
    public function getOpSys()
    {
        return $this->opSys;
    }

    /**
     * Set dupId
     *
     * @param integer $dupId
     */
    public function setDupId($dupId)
    {
        $this->dupId = $dupId;
    }

    /**
     * Get dupId
     *
     * @return integer 
     */
    public function getDupId()
    {
        return $this->dupId;
    }

    /**
     * Set bugFileLoc
     *
     * @param string $bugFileLoc
     */
    public function setBugFileLoc($bugFileLoc)
    {
        $this->bugFileLoc = $bugFileLoc;
    }

    /**
     * Get bugFileLoc
     *
     * @return string 
     */
    public function getBugFileLoc()
    {
        return $this->bugFileLoc;
    }

    /**
     * Set statusWhiteboard
     *
     * @param string $statusWhiteboard
     */
    public function setStatusWhiteboard($statusWhiteboard)
    {
        $this->statusWhiteboard = $statusWhiteboard;
    }

    /**
     * Get statusWhiteboard
     *
     * @return string 
     */
    public function getStatusWhiteboard()
    {
        return $this->statusWhiteboard;
    }

    /**
     * Set targetMilestone
     *
     * @param string $targetMilestone
     */
    public function setTargetMilestone($targetMilestone)
    {
        $this->targetMilestone = $targetMilestone;
    }

    /**
     * Get targetMilestone
     *
     * @return string 
     */
    public function getTargetMilestone()
    {
        return $this->targetMilestone;
    }

    /**
     * Set votes
     *
     * @param integer $votes
     */
    public function setVotes($votes)
    {
        $this->votes = $votes;
    }

    /**
     * Get votes
     *
     * @return integer 
     */
    public function getVotes()
    {
        return $this->votes;
    }

    /**
     * Set everconfirmed
     *
     * @param string $everconfirmed
     */
    public function setEverconfirmed($everconfirmed)
    {
        $this->everconfirmed = $everconfirmed;
    }

    /**
     * Get everconfirmed
     *
     * @return string 
     */
    public function getEverconfirmed()
    {
        return $this->everconfirmed;
    }

    /**
     * Set qaContact
     *
     * @param string $qaContact
     */
    public function setQaContact($qaContact)
    {
        $this->qaContact = $qaContact;
    }

    /**
     * Get qaContact
     *
     * @return string 
     */
    public function getQaContact()
    {
        return $this->qaContact;
    }

    /**
     * Set estimatedTime
     *
     * @param string $estimatedTime
     */
    public function setEstimatedTime($estimatedTime)
    {
        $this->estimatedTime = $estimatedTime;
    }

    /**
     * Get estimatedTime
     *
     * @return string 
     */
    public function getEstimatedTime()
    {
        return $this->estimatedTime;
    }

    /**
     * Set remainingTime
     *
     * @param string $remainingTime
     */
    public function setRemainingTime($remainingTime)
    {
        $this->remainingTime = $remainingTime;
    }

    /**
     * Get remainingTime
     *
     * @return string 
     */
    public function getRemainingTime()
    {
        return $this->remainingTime;
    }

    /**
     * Set actualTime
     *
     * @param datetime $actualTime
     */
    public function setActualTime($actualTime)
    {
        $this->actualTime = $actualTime;
    }

    /**
     * Get actualTime
     *
     * @return datetime 
     */
    public function getActualTime()
    {
        return $this->actualTime;
    }

    /**
     * Set deadline
     *
     * @param datetime $deadline
     */
    public function setDeadline($deadline)
    {
        $this->deadline = $deadline;
    }

    /**
     * Get deadline
     *
     * @return datetime 
     */
    public function getDeadline()
    {
        return $this->deadline;
    }

    /**
     * Set keywords
     *
     * @param string $keywords
     */
    public function setKeywords($keywords)
    {
        $this->keywords = $keywords;
    }

    /**
     * Get keywords
     *
     * @return string 
     */
    public function getKeywords()
    {
        return $this->keywords;
    }

    /**
     * Set flag
     *
     * @param string $flag
     */
    public function setFlag($flag)
    {
        $this->flag = $flag;
    }

    /**
     * Get flag
     *
     * @return string 
     */
    public function getFlag()
    {
        return $this->flag;
    }

    /**
     * Set cc
     *
     * @param string $cc
     */
    public function setCc($cc)
    {
        $this->cc = $cc;
    }

    /**
     * Get cc
     *
     * @return string 
     */
    public function getCc()
    {
        return $this->cc;
    }

    /**
     * Set groupBugzilla
     *
     * @param string $groupBugzilla
     */
    public function setGroupBugzilla($groupBugzilla)
    {
        $this->groupBugzilla = $groupBugzilla;
    }

    /**
     * Get groupBugzilla
     *
     * @return string 
     */
    public function getGroupBugzilla()
    {
        return $this->groupBugzilla;
    }

    /**
     * Set issueId
     *
     * @param integer $issueId
     */
    public function setIssueId($issueId)
    {
        $this->issueId = $issueId;
    }

    /**
     * Get issueId
     *
     * @return integer 
     */
    public function getIssueId()
    {
        return $this->issueId;
    }
}