<?php

namespace Iccs\BichoDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\BichoDbBundle\Entity\Changes
 */
class Changes
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var integer $issueId
     */
    private $issueId;

    /**
     * @var string $field
     */
    private $field;

    /**
     * @var string $oldValue
     */
    private $oldValue;

    /**
     * @var string $newValue
     */
    private $newValue;

    /**
     * @var integer $changedBy
     */
    private $changedBy;

    /**
     * @var datetime $changedOn
     */
    private $changedOn;


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

    /**
     * Set field
     *
     * @param string $field
     */
    public function setField($field)
    {
        $this->field = $field;
    }

    /**
     * Get field
     *
     * @return string 
     */
    public function getField()
    {
        return $this->field;
    }

    /**
     * Set oldValue
     *
     * @param string $oldValue
     */
    public function setOldValue($oldValue)
    {
        $this->oldValue = $oldValue;
    }

    /**
     * Get oldValue
     *
     * @return string 
     */
    public function getOldValue()
    {
        return $this->oldValue;
    }

    /**
     * Set newValue
     *
     * @param string $newValue
     */
    public function setNewValue($newValue)
    {
        $this->newValue = $newValue;
    }

    /**
     * Get newValue
     *
     * @return string 
     */
    public function getNewValue()
    {
        return $this->newValue;
    }

    /**
     * Set changedBy
     *
     * @param integer $changedBy
     */
    public function setChangedBy($changedBy)
    {
        $this->changedBy = $changedBy;
    }

    /**
     * Get changedBy
     *
     * @return integer 
     */
    public function getChangedBy()
    {
        return $this->changedBy;
    }

    /**
     * Set changedOn
     *
     * @param datetime $changedOn
     */
    public function setChangedOn($changedOn)
    {
        $this->changedOn = $changedOn;
    }

    /**
     * Get changedOn
     *
     * @return datetime 
     */
    public function getChangedOn()
    {
        return $this->changedOn;
    }
}