<?php

namespace Iccs\CvsanalyDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\CvsanalyDbBundle\Entity\Actions
 */
class Actions
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var string $type
     */
    private $type;

    /**
     * @var integer $fileId
     */
    private $fileId;

    /**
     * @var integer $commitId
     */
    private $commitId;

    /**
     * @var integer $branchId
     */
    private $branchId;


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
     * Set fileId
     *
     * @param integer $fileId
     */
    public function setFileId($fileId)
    {
        $this->fileId = $fileId;
    }

    /**
     * Get fileId
     *
     * @return integer 
     */
    public function getFileId()
    {
        return $this->fileId;
    }

    /**
     * Set commitId
     *
     * @param integer $commitId
     */
    public function setCommitId($commitId)
    {
        $this->commitId = $commitId;
    }

    /**
     * Get commitId
     *
     * @return integer 
     */
    public function getCommitId()
    {
        return $this->commitId;
    }

    /**
     * Set branchId
     *
     * @param integer $branchId
     */
    public function setBranchId($branchId)
    {
        $this->branchId = $branchId;
    }

    /**
     * Get branchId
     *
     * @return integer 
     */
    public function getBranchId()
    {
        return $this->branchId;
    }
}