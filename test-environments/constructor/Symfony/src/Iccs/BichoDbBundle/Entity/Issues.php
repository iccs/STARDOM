<?php

namespace Iccs\BichoDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\BichoDbBundle\Entity\Issues
 */
class Issues
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var integer $trackerId
     */
    private $trackerId;

    /**
     * @var string $issue
     */
    private $issue;

    /**
     * @var string $type
     */
    private $type;

    /**
     * @var string $summary
     */
    private $summary;

    /**
     * @var text $description
     */
    private $description;

    /**
     * @var string $status
     */
    private $status;

    /**
     * @var string $resolution
     */
    private $resolution;

    /**
     * @var string $priority
     */
    private $priority;

    /**
     * @var integer $submittedBy
     */
    private $submittedBy;

    /**
     * @var datetime $submittedOn
     */
    private $submittedOn;

    /**
     * @var integer $assignedTo
     */
    private $assignedTo;


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
     * Set trackerId
     *
     * @param integer $trackerId
     */
    public function setTrackerId($trackerId)
    {
        $this->trackerId = $trackerId;
    }

    /**
     * Get trackerId
     *
     * @return integer 
     */
    public function getTrackerId()
    {
        return $this->trackerId;
    }

    /**
     * Set issue
     *
     * @param string $issue
     */
    public function setIssue($issue)
    {
        $this->issue = $issue;
    }

    /**
     * Get issue
     *
     * @return string 
     */
    public function getIssue()
    {
        return $this->issue;
    }

    /**
     * Set type
     *
     * @param string $type
     */
    public function setType($type)
    {
        $this->type = $type;
    }

    /**
     * Get type
     *
     * @return string 
     */
    public function getType()
    {
        return $this->type;
    }

    /**
     * Set summary
     *
     * @param string $summary
     */
    public function setSummary($summary)
    {
        $this->summary = $summary;
    }

    /**
     * Get summary
     *
     * @return string 
     */
    public function getSummary()
    {
        return $this->summary;
    }

    /**
     * Set description
     *
     * @param text $description
     */
    public function setDescription($description)
    {
        $this->description = $description;
    }

    /**
     * Get description
     *
     * @return text 
     */
    public function getDescription()
    {
        return $this->description;
    }

    /**
     * Set status
     *
     * @param string $status
     */
    public function setStatus($status)
    {
        $this->status = $status;
    }

    /**
     * Get status
     *
     * @return string 
     */
    public function getStatus()
    {
        return $this->status;
    }

    /**
     * Set resolution
     *
     * @param string $resolution
     */
    public function setResolution($resolution)
    {
        $this->resolution = $resolution;
    }

    /**
     * Get resolution
     *
     * @return string 
     */
    public function getResolution()
    {
        return $this->resolution;
    }

    /**
     * Set priority
     *
     * @param string $priority
     */
    public function setPriority($priority)
    {
        $this->priority = $priority;
    }

    /**
     * Get priority
     *
     * @return string 
     */
    public function getPriority()
    {
        return $this->priority;
    }

    /**
     * Set submittedBy
     *
     * @param integer $submittedBy
     */
    public function setSubmittedBy($submittedBy)
    {
        $this->submittedBy = $submittedBy;
    }

    /**
     * Get submittedBy
     *
     * @return integer 
     */
    public function getSubmittedBy()
    {
        return $this->submittedBy;
    }

    /**
     * Set submittedOn
     *
     * @param datetime $submittedOn
     */
    public function setSubmittedOn($submittedOn)
    {
        $this->submittedOn = $submittedOn;
    }

    /**
     * Get submittedOn
     *
     * @return datetime 
     */
    public function getSubmittedOn()
    {
        return $this->submittedOn;
    }

    /**
     * Set assignedTo
     *
     * @param integer $assignedTo
     */
    public function setAssignedTo($assignedTo)
    {
        $this->assignedTo = $assignedTo;
    }

    /**
     * Get assignedTo
     *
     * @return integer 
     */
    public function getAssignedTo()
    {
        return $this->assignedTo;
    }
}